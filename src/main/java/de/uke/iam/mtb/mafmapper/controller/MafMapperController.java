package de.uke.iam.mtb.mafmapper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.uke.iam.mtb.dto.miracum.MiracumMafDto;
import de.uke.iam.mtb.mafmapper.service.MafMapperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class MafMapperController {

    @Autowired
    private final MafMapperService mapperService;

    public MafMapperController(MafMapperService mapperService) {
        this.mapperService = mapperService;
    }

    @Operation(summary = "Maps the MIRACUM mafs to the KC mafs", description = "Maps the MIRACUM mafs to the KC mafs and sends them to the KC. Extracts the biosample data and sends it to the KC")
    @ApiResponse(responseCode = "200", description = "Mapped <size of the maf list> MIRACUM mafs to list of KC mafs and sent them. Extracted the biosample data and sent it to KC")
    @PostMapping("maf_mapper/map")
    public ResponseEntity<String> mapMafs(
            @Parameter(description = "List of MiracumMafDtos. Can be empty, but not null", required = true, content = @Content(array = @ArraySchema(schema = @Schema(implementation = MiracumMafDto.class)))) @RequestBody List<MiracumMafDto> miracumMafDtos) {
        mapperService.mapAndSend(miracumMafDtos);
        return new ResponseEntity<String>(
                "Mapped " + miracumMafDtos.size()
                        + " MIRACUM mafs to list of KC mafs and sent them. Extracted the biosample data and sent it to KC",
                HttpStatus.OK);
    }
}
