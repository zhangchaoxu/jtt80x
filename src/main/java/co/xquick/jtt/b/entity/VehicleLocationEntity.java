package co.xquick.jtt.b.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 车辆
 *
 * @author Charles zhangchaoxu@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("b_vehicle_location")
public class VehicleLocationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;
	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long  creator;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createDate;
	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
	/**
	 * 软删标记  1：已删除  0：正常
	 */
	@TableField(fill = FieldFill.INSERT)
	@TableLogic
	private Integer delFlag;
	/**
	 * 原始数据
	 */
	private String raw;
	/**
	 * 车辆id
	 */
	private Long vehicleId;
	/**
	 * 车牌号
	 */
	private String vehicleNo;
	/**
	 * 经度
	 */
	private Double longitude;
	/**
	 * 纬度
	 */
	private Double latitude;
	/**
	 * 设备id
	 */
	private String deviceNo;
	/**
	 * 定位时间
	 */
	private Date locationTime;
	/**
	 * 报警状态
	 */
	private Integer alarm;
	/**
	 * 车辆状态
	 */
	private Integer state;
	/**
	 * 海拔高度
	 */
	private Integer altitude;
	/**
	 * 方向
	 */
	private Integer direction;
	/**
	 * 车辆当前总里程数
	 */
	private Integer vec3;
	/**
	 * 速度
	 */
	private Integer vec2;
	/**
	 * 卫星速度
	 */
	private Integer vec1;

}