package com.ddkolesnik.trading.model;

import com.ddkolesnik.trading.model.dto.CadasterDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "cadaster")
@EqualsAndHashCode(callSuper = true)
public class CadasterEntity extends BaseEntity {

    @Column(name = "address")
    private String address;

    @Column(name = "cad_number")
    private String cadNumber;

    @Column(name = "type")
    private String type;

    @Column(name = "area")
    private String area;

    @Column(name = "category")
    private String category;

    @Column(name = "tag")
    private String tag;

    public CadasterEntity(CadasterDTO dto, String tag) {
        this.address = dto.getAddress();
        this.cadNumber = dto.getCadNumber();
        this.type = dto.getType();
        this.area = dto.getArea();
        this.category = dto.getCategory();
        this.tag = tag;
    }

}
