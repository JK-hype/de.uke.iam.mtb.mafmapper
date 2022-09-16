package de.uke.iam.mtb.mafmapper.service;

import static de.uke.iam.lib.restclient.RESTUtilHelper.post;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.uke.iam.lib.json.GsonHelper;
import de.uke.iam.mtb.dto.kc.KcBiosampleDto;
import de.uke.iam.mtb.dto.kc.KcMafDto;
import de.uke.iam.mtb.dto.miracum.MiracumMafDto;
import de.uke.iam.mtb.mafmapper.mapper.MiracumMafToKcMafMapper;

@Service
public class MafMapperService {

    private final String kcUrl;

    public MafMapperService(@Value("${kcUrl}") String kcUrl) {
        this.kcUrl = kcUrl;
    }

    public void mapAndSend(List<MiracumMafDto> miracumMafDtos) {
        List<KcMafDto> kcMafDtos = miracumMafDtos.stream().map(MiracumMafToKcMafMapper::mapToKcMaf)
                .collect(Collectors.toList());

        // Filters all mafDtos with redundant biosampleId
        List<String> biosampleIds = new ArrayList<>();
        List<KcBiosampleDto> kcBiosampleDtos = miracumMafDtos.stream().map((dto) -> {
            if (biosampleIds.stream().noneMatch((id) -> id.equals(dto.getTumorSampleBarcode()))) {
                biosampleIds.add(dto.getTumorSampleBarcode());
                return MiracumMafToKcMafMapper.mapToKcBiosample(dto);
            }
            return null;
        }).filter((dto) -> dto != null).collect(Collectors.toList());

        String mafJson = GsonHelper.get().getNewGson().toJson(kcMafDtos);
        String biosampleJson = GsonHelper.get().getNewGson().toJson(kcBiosampleDtos);

        try {
            post(new URL(kcUrl + "/mafs"), mafJson);
            post(new URL(kcUrl + "/biosamples"), biosampleJson);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
