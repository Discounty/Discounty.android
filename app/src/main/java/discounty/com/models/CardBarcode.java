package discounty.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * This class represents the Barcode model.
 * It consists of fields that are returned from
 * the correspondent API (/customers/full_info) and
 * getters and setters to them.
 */
public class CardBarcode {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("barcode")
    @Expose
    private String barcode;

    @SerializedName("discount_percentage")
    @Expose
    private String discountPercentage;

    @SerializedName("extra_info")
    @Expose
    private String extraInfo;

    @SerializedName("barcode_type_id")
    @Expose
    private Integer barcodeTypeId;

    @SerializedName("discount_card_id")
    @Expose
    private Integer discountCardId;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("barcode_type")
    @Expose
    private CardBarcodeType barcodeType;



    public CardBarcodeType getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(CardBarcodeType barcodeType) {
        this.barcodeType = barcodeType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public Integer getBarcodeTypeId() {
        return barcodeTypeId;
    }

    public void setBarcodeTypeId(Integer barcodeTypeId) {
        this.barcodeTypeId = barcodeTypeId;
    }

    public Integer getDiscountCardId() {
        return discountCardId;
    }

    public void setDiscountCardId(Integer discountCardId) {
        this.discountCardId = discountCardId;
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
        return "CardBarcode{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", discountPercentage='" + discountPercentage + '\'' +
                ", extraInfo='" + extraInfo + '\'' +
                ", barcodeTypeId=" + barcodeTypeId +
                ", discountCardId=" + discountCardId +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
