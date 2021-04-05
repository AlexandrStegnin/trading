package com.ddkolesnik.trading.api;

import com.ddkolesnik.trading.model.BaseEntity;
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

    @Column(name = "floor")
    private String floor;

    @Column(name = "split_address")
    private String splitAddress;

    public CadasterEntity(CadasterDTO dto, String tag, String modifiedBy) {
        this.address = dto.getAddress();
        this.cadNumber = dto.getCadNumber();
        this.type = dto.getType();
        this.area = dto.getArea();
        this.category = dto.getCategory();
        this.tag = tag;
        this.setModifiedBy(modifiedBy);
    }

    public CadasterEntity(KadnetData dto, String tag, String modifiedBy) {
        this.address = dto.getAddress();
        this.cadNumber = dto.getKadNumber();
        this.type = dto.getObjectType();
        this.area = dto.getArea().toString();
        this.category = dto.getName();
        this.tag = tag;
        this.floor = dto.getFloor();
        this.setModifiedBy(modifiedBy);
    }

}
