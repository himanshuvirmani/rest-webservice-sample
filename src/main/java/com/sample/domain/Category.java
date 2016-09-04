package com.sample.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created by himanshu.virmani on 04/09/16.
 */
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Category extends BaseEntity {

    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotEmpty
    @Column(name = "description")
    private String description;

/*    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonProperty(value = "products")
    private List<Product> products = new ArrayList<>();*/

    //for category path
    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="parent_id")
    private Category parent;

/*    // list of subcategories
    @OneToMany(mappedBy="parent")
    private Set<Category> categories = new HashSet<>();*/

}
