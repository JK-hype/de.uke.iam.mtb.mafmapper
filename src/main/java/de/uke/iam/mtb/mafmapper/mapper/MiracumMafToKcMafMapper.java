package de.uke.iam.mtb.mafmapper.mapper;

import de.uke.iam.mtb.dto.kc.KcBiosampleDto;
import de.uke.iam.mtb.dto.kc.KcMafDto;
import de.uke.iam.mtb.dto.miracum.MiracumMafDto;

public class MiracumMafToKcMafMapper {

    /*
     * Codons and RefSeq can be null
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
        kcMafDto.setStart(Long.valueOf(miracumMafDto.getStartPosition()));
        kcMafDto.setEnd(Long.valueOf(miracumMafDto.getEndPosition()));
        kcMafDto.setRefGenome(miracumMafDto.getNcbiBuild());
        kcMafDto.setRefAllele(miracumMafDto.getReferenceAllele());
        kcMafDto.setTumorSeqAllele1(miracumMafDto.getTumorSeqAllele1());
        kcMafDto.setTumorSeqAllele2(miracumMafDto.getTumorSeqAllele2());
        kcMafDto.setVariantOnGene(miracumMafDto.getTxChange().split("c.")[1]);
        kcMafDto.setVariantOnProtein(miracumMafDto.getHgvspShort().split("p.")[1]);
        kcMafDto.setDbsnpRs(miracumMafDto.getDbsnpRs());
        kcMafDto.setNDepth(
                Integer.parseInt(miracumMafDto.getAltCountN()));
        kcMafDto.setTDepth(
                Integer.parseInt(miracumMafDto.getAltCountT()));
        kcMafDto.setProteinPosition(miracumMafDto.getAminoAcidChange());
        kcMafDto.setStrand(miracumMafDto.getStrand());
        kcMafDto.setConsequence(miracumMafDto.getVariantClassification());
        return kcMafDto;
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