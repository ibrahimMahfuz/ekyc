package id.co.pcsindonesia.ia.ekyc.dto.query;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VidaStatusOcrDto {
    private Long nik;
    private String provinsi;
    private String kabupatenKota;
    private String golonganDarah;
    private String agama;
    private String alamat;
    private String berlakuHingga;
    private String kewarganegaraan;
    private String nama;
    private String pekerjaan;
    private String tempatLahir;
    private String kecamatan;
    private String jenisKelamin;
    private String rtRw;
    private String tanggalLahir;
    private String statusPerkawinan;
    private String kelurahanDesa;
}
