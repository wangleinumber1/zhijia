package com.zhijia.Enum;

/**
 * 区域类型的枚举
 */
public enum AreaTypeEnum {

    AREA("area"), PRICE("price"), TYPE("type"), FEATURE("feature"),DECORATE("decorate"),CIRCLELINE("circleLine"),ORDER("order"),OPENTIME("opentime"),CIRCLE("circle"),UNDEFINED("undefined"),

    PROPORTION("proportion"),ROOM("room"),ASPECT("aspect"),HOUSEAGE("houseage"),FLOOR("floor"),SOURCE("source"),TAG("tag"),PROJECT_TYPE("project_type"),
    RENT_YPE("rent_type"),AVERAGE_PRICE("average_price"),SELL_PRICE("sellprice"),RENT_TYPE("renttype"),SELLPRICE("sellprice"),RENTPRICE("rentprice");

    private String value;

    AreaTypeEnum(String value) {
        this.value = value;
    }


    public static AreaTypeEnum getAreaType(String value) {
        for (AreaTypeEnum temp : AreaTypeEnum.values()) {
            if (temp.value.equalsIgnoreCase(value)) {
                return temp;
            }
        }
        return UNDEFINED;
    }
}
