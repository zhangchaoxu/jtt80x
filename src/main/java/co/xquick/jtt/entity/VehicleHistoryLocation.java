package co.xquick.jtt.entity;

import co.xquick.jtt.utils.ByteBufPool;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆补报定位
 */
@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class VehicleHistoryLocation extends Vehicle {

    /**
     * 构造VehicleCurrentLocation对象，这里的msgBody的下标必须已经移动到该对象对应的数据部分
     *
     * @param vehicleNo
     * @param vehicleColor
     * @param msgBody
     */
    public VehicleHistoryLocation(String vehicleNo, byte vehicleColor, int dataLength, ByteBuf msgBody) {
        this.vehicleNo = vehicleNo;
        this.vehicleColor = vehicleColor;
        this.dataType = JT809Constants.UP_EXG_MSG_HISTORY_LOCATION;
        this.dataLength = dataLength;
        gnssCnt = msgBody.readByte();
        gnssDataList = new ArrayList<>(gnssCnt);
        VehicleLocation location;
        for (int i = 0; i < gnssCnt; i++) {
            ByteBuf byteBuf = ByteBufPool.BYTE_BUF_POOL.buffer(36);
            msgBody.readBytes(byteBuf);
            location = new VehicleLocation(byteBuf);
            gnssDataList.add(location);
        }
    }

    /**
     * 定位上传
     */
    @Override
    public int handlerBiz(String orgKey) {
        List<LbsUploadLocationRequest> request = Lists.newArrayList();
        LbsUploadLocationRequest location;
        for (VehicleLocation vehhicleLocation : gnssDataList) {
            location = vehhicleLocation.convertLocation(vehicleNo);
            request.add(location);
        }
        LOGGER.info("获得历史定位，{}", JSON.toJSONString(request));
        return request.size();

    }

    /**
     * 包含的定位数据个数
     */
    private byte gnssCnt;

    private List<VehicleLocation> gnssDataList;

}
