package discounty.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * This class represents the DiscountCard model.
 * It consists of fields that are returned from
 * the correspondent API (/customers/full_info) and
 * getters and setters to them.
 */
public class DiscountCard {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("shop_id")
    @Expose
    private Integer shopId;

    @SerializedName("customer_id")
    @Expose
    private Integer customerId;

    @SerializedName("unregistered_shop")
    @Expose
    private String unregisteredShop;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("barcode")
    @Expose
    private CardBarcode barcode;


    public CardBarcode getBarcode() {
        return barcode;
    }

    public void setBarcode(CardBarcode barcode) {
        this.barcode = barcode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getUnregisteredShop() {
        return unregisteredShop;
    }

    public void setUnregisteredShop(String unregisteredShop) {
        this.unregisteredShop = unregisteredShop;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "DiscountCard{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", shopId=" + shopId +
                ", customerId=" + customerId +
                ", unregisteredShop='" + unregisteredShop + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", barcode='" + barcode + '\'' +
                '}';
    }
}
