package discounty.com.data.models;

import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Shops", id = BaseColumns._ID)
public class Shop extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "needsSync")
    public boolean needsSync;

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

    @Column(name = "ServerId")
    public Integer serverId;

    @Column(name = "CreatedAt")
    public Long createdAt;

    @Column(name = "UpdatedAt")
    public Long updatedAt;

    public Shop() {
        super();
    }

    public Shop(String name, String description, String chainStore, String address, String city,
                String country, Integer serverId, Long createdAt, Long updatedAt, boolean needsSync) {
        super();
        this.name = name;
        this.description = description;
        this.chainStore = chainStore;
        this.address = address;
        this.city = city;
        this.country = country;
        this.serverId = serverId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.needsSync = needsSync;
    }
}
