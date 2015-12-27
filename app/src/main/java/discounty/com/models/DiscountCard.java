package discounty.com.models;

import com.google.gson.annotations.SerializedName;

/*
 * This class represents the DiscountCard model.
 * It consists of fields that are returned from
 * the correspondent API (/customers/full_info) and
 * getters and setters to them.
 */
public class DiscountCard {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("shop_id")
    private Integer shopId;

    @SerializedName("customer_id")
    private Integer customerId;

    @SerializedName("unregistered_shop")
    private String unregisteredShop;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

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
                '}';
    }
}
