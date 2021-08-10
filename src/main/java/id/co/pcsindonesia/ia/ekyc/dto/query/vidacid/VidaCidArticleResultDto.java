package id.co.pcsindonesia.ia.ekyc.dto.query.vidacid;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VidaCidArticleResultDto {
    private String name;
    private Double score;
    private Integer weight;
    private VidaCidArticleResultExtraDto extra;
}
