// ENUM  可以看做一个MySQL数据库
// CountryEnum  可以一看做一个表

public enum CountryEnum {
    ONE(1,"齐"),
    TWO(2,"楚"),
    THREE(3,"燕"),
    FOUR(4,"韩"),
    FIVE(5,"赵"),
    SIX(6,"魏");

    public static CountryEnum forEach_CountryEnum(int index){
        CountryEnum[] myarray = CountryEnum.values();
        for (CountryEnum countryEnum : myarray) {
            if(index == countryEnum.getRetCode()){
                return countryEnum;
            }

        }
        return null;
    }
    private Integer retCode;
    private String retMessage;

    public Integer getRetCode() {
        return retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    CountryEnum(Integer retCode, String retMessage) {
        this.retCode = retCode;
        this.retMessage = retMessage;
    }
}
