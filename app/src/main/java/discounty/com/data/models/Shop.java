package discounty.com.data.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Shops")
public class Shop extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "Description")
    public String description;

    @Column(name = "ChainStore")
    public String chainStore;

    @Column(name = "Address")
    public String address;

    @Column(name = "City")
    public String city;

    @Column(name = "Country")
    public String country;

    @Column(name = "CreatedAt")
    public Long createdAt;

    @Column(name = "UpdatedAt")
    public Long updatedAt;

    public Shop() {
        super();
    }

    public Shop(String name, String description, String chainStore, String address, String city, String country, Long createdAt, Long updatedAt) {
        super();
        this.name = name;
        this.description = description;
        this.chainStore = chainStore;
        this.address = address;
        this.city = city;
        this.country = country;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
