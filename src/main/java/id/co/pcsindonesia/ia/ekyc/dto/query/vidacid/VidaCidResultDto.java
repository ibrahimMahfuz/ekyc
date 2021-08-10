package id.co.pcsindonesia.ia.ekyc.dto.query.vidacid;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VidaCidResultDto {
    private Boolean match;
    private Double score;
    private Double threshold;
    private List<VidaCidArticleResultDto> articlesResult;

}
