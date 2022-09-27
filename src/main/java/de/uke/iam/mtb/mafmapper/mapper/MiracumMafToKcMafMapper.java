package de.uke.iam.mtb.mafmapper.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uke.iam.mtb.dto.kc.KcBiosampleDto;
import de.uke.iam.mtb.dto.kc.KcMafDto;
import de.uke.iam.mtb.dto.miracum.MiracumMafDto;

public class MiracumMafToKcMafMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MiracumMafToKcMafMapper.class);

    /*
     * codons, proteinPosition and refSeq can be null
     */
    public static KcMafDto mapToKcMaf(MiracumMafDto miracumMafDto) {
        KcMafDto kcMafDto = new KcMafDto();
        kcMafDto.setPatientId(miracumMafDto.getPatientId());
        kcMafDto.setBiosampleId(miracumMafDto.getTumorSampleBarcode());
        kcMafDto.setGenes(miracumMafDto.getHugoSymbol().split(","));
        kcMafDto.setTranscript(miracumMafDto.getTranscriptId());
        kcMafDto.setVariantClassification(
                miracumMafDto.getVariantType().replace("SNP", "SNV").replace("DEL", "INDEL").toUpperCase());
        kcMafDto.setChromosome(miracumMafDto.getChromosome());
        kcMafDto.setVariantType(miracumMafDto.getVariantType());
        try {
            kcMafDto.setStart(Long.valueOf(miracumMafDto.getStartPosition()));
        } catch (NumberFormatException e) {
            LOGGER.warn("Could not map startPosition");
        }
        try {
            kcMafDto.setEnd(Long.valueOf(miracumMafDto.getEndPosition()));
        } catch (NumberFormatException e) {
            LOGGER.warn("Could not map endPosition");
        }
        kcMafDto.setRefGenome(miracumMafDto.getNcbiBuild());
        kcMafDto.setRefAllele(miracumMafDto.getReferenceAllele());
        kcMafDto.setTumorSeqAllele1(miracumMafDto.getTumorSeqAllele1());
        kcMafDto.setTumorSeqAllele2(miracumMafDto.getTumorSeqAllele2());
        kcMafDto.setVariantOnGene(indexOutOfBoundCheck(miracumMafDto.getTxChange().split("c.")));
        kcMafDto.setVariantOnProtein(indexOutOfBoundCheck(miracumMafDto.getAminoAcidChange().split("p.")));
        kcMafDto.setDbsnpRs(miracumMafDto.getDbsnpRs());
        try {
            kcMafDto.setNDepth(
                    Integer.parseInt(miracumMafDto.getAltCountN()));
        } catch (NumberFormatException e) {
            LOGGER.warn("Could not map nDepth");
        }
        try {
            kcMafDto.setTDepth(
                    Integer.parseInt(miracumMafDto.getAltCountT()));
        } catch (NumberFormatException e) {
            LOGGER.warn("Could not map tDepth");
        }
        kcMafDto.setStrand(miracumMafDto.getStrand());
        kcMafDto.setConsequence(miracumMafDto.getVariantClassification());
        return kcMafDto;
    }

    private static String indexOutOfBoundCheck(String[] split) {
        if (split.length > 1) {
            return split[1];
        } else {
            return "";
        }
    }

    public static KcBiosampleDto mapToKcBiosample(MiracumMafDto miracumMafDto) {
        KcBiosampleDto kcBiosampleDto = new KcBiosampleDto();
        kcBiosampleDto.setId(miracumMafDto.getTumorSampleBarcode());
        kcBiosampleDto.setPatientId(miracumMafDto.getPatientId());
        kcBiosampleDto.setSequencing(miracumMafDto.getProtocol().name());
        kcBiosampleDto.setSequenced(true);
        return kcBiosampleDto;
    }

    private MiracumMafToKcMafMapper() {
    }
}
