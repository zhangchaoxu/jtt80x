package co.xquick.jtt.entity;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 上传定位信息请求
 */
@Data
public class LbsUploadLocationRequest {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 设备号
     */
    private String deviceId;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 拼接设备，有该字段时，将该字段拼接到设备ID上
     */
    private String xxx;
    /**
     * 纬度
     */
    private String latitude;

    private String sourceLongitude;

    private String sourceLatitude;
    /**
     * 定位时间
     */
    private String locationTime;
    private Date locationTimeDate;

    private String deviceType;
    /**
     * 2016-06-20新增字段 城市
     */
    private String city;
    /**
     * 2016-06-20新增字段 省份
     */
    private String province;
    /**
     * 2016-06-20新增字段 地址
     */
    private String position;
    /**
     * 温度
     */
    private String[] temperatures;
    /**
     * 湿度
     */
    private String[] humidity;
    /**
     * 开门预警 ["1","x","0"]
     */
    private String[] doorsensorStatus;
    /**
     * 密码
     */
    private String doorsensorPassword;

    private String operatorName;

    public void setLocationTimeDate(Date locationTimeDate) {
        this.locationTimeDate = locationTimeDate;
        this.locationTime = format.format(locationTimeDate);
    }

    public void setLocationTime(String locationTime) {
        this.locationTime = locationTime;
        try {
            this.locationTimeDate = format.parse(locationTime);
        } catch (ParseException e) {
            throw new RuntimeException("定位时间格式错误转换失败");
        }
    }

}
