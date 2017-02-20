package com.zhijia.service.data.Medol;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 新房详情JSON
 */
public class NewHouseDetailJsonModel {

    private SlideJsonModel slide;

    private UnitsJsonModel units;

    //限时特惠楼盘(0-否，1-是)
    private String discount;

    private String clientService;

    private String clientServiceTell;

    private  BusinessJsonModel business ;

    private String tuanid ;

    private TuanJsonModel tuan ;

    private NewsJsonModel news ;

    private ImpressionJsonModel impression ;

    private WendaJsonModel wenda ;

    private DianpingJsonModel dianping ;

    private TrendJsonModel trend ;

    private IntroductionJsonModel introduction ;

    private DetailJsonModel detail ;

    private MapJsonModel map ;

    @JsonProperty(value = "slide")
    public SlideJsonModel getSlide() {
        return slide;
    }

    public void setSlide(SlideJsonModel slide) {
        this.slide = slide;
    }

    @JsonProperty(value = "units")
    public UnitsJsonModel getUnits() {
        return units;
    }

    public void setUnits(UnitsJsonModel units) {
        this.units = units;
    }

    @JsonProperty(value = "discount")
    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    @JsonProperty(value = "400")
    public String getClientService() {
        return clientService;
    }

    public void setClientService(String clientService) {
        this.clientService = clientService;
    }

    @JsonProperty(value = "400tel")
    public String getClientServiceTell() {
        return clientServiceTell;
    }

    public void setClientServiceTell(String clientServiceTell) {
        this.clientServiceTell = clientServiceTell;
    }

    @JsonProperty(value = "business")
    public BusinessJsonModel getBusiness() {
        return business;
    }

    public void setBusiness(BusinessJsonModel business) {
        this.business = business;
    }

    @JsonProperty(value = "tuanid")
    public String getTuanid() {
        return tuanid;
    }

    public void setTuanid(String tuanid) {
        this.tuanid = tuanid;
    }

    @JsonProperty(value = "tuan")
    public TuanJsonModel getTuan() {
        return tuan;
    }

    public void setTuan(TuanJsonModel tuan) {
        this.tuan = tuan;
    }

    @JsonProperty(value = "news")
    public NewsJsonModel getNews() {
        return news;
    }

    public void setNews(NewsJsonModel news) {
        this.news = news;
    }

    @JsonProperty(value = "impression")
    public ImpressionJsonModel getImpression() {
        return impression;
    }

    public void setImpression(ImpressionJsonModel impression) {
        this.impression = impression;
    }

    @JsonProperty(value = "wenda")
    public WendaJsonModel getWenda() {
        return wenda;
    }

    public void setWenda(WendaJsonModel wenda) {
        this.wenda = wenda;
    }

    @JsonProperty(value = "dianping")
    public DianpingJsonModel getDianping() {
        return dianping;
    }

    public void setDianping(DianpingJsonModel dianping) {
        this.dianping = dianping;
    }

    @JsonProperty(value = "trend")
    public TrendJsonModel getTrend() {
        return trend;
    }

    public void setTrend(TrendJsonModel trend) {
        this.trend = trend;
    }

    @JsonProperty(value = "introduction")
    public IntroductionJsonModel getIntroduction() {
        return introduction;
    }

    public void setIntroduction(IntroductionJsonModel introduction) {
        this.introduction = introduction;
    }

    @JsonProperty(value = "detail")
    public DetailJsonModel getDetail() {
        return detail;
    }

    public void setDetail(DetailJsonModel detail) {
        this.detail = detail;
    }

    @JsonProperty(value = "map")
    public MapJsonModel getMap() {
        return map;
    }

    public void setMap(MapJsonModel map) {
        this.map = map;
    }
}
