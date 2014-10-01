package vn.onepay.card.charging.entities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargeStatus {

	public final static String SUCCESS_STATUS = "00";//Giao dịch thành công.
	public final static String INVALID_IP_STATUS = "01";//Lỗi, địa chỉ IP truy cập API bị từ chối.
	public final static String INVALID_INPUT_PARAMS_STATUS = "02";//Lỗi, tham số gửi từ merchant tới chưa chính xác (thường sai tên tham số hoặc thiếu tham số)
	public final static String MERCHANT_LOCKED_OR_NOT_EXISTED_STATUS = "03";//Lỗi, merchant không tồn tại hoặc merchant đang bị khóa kết nối.
	public final static String INVALID_PASSWORD_OR_SIGNATURE_STATUS = "04";//Mật khẩu hoặc chữ ký xác thực không chính xác.
	public final static String TRAN_REF_IS_DUPLICATED_STATUS = "05";//Trùng mã giao dịch (transRef).
	public final static String TRAN_REF_NOT_EXISTED_OR_INVALID_STATUS = "06";//Mã giao dịch không tồn tại hoặc sai định dạng

	public final static String CARD_IS_USED_OR_NOT_EXISTED_STATUS = "07";//Thẻ đã được sử dụng, hoặc thẻ sai.
	public final static String CARD_LOCKED_STATUS = "08";//Thẻ bị khóa
	public final static String CARD_EXPIRED_STATUS = "09";//Thẻ hết hạn sử dụng.
	public final static String CARD_NOT_ACTIVED_OR_NOT_EXISTED_STATUS = "10";//Thẻ chưa được kích hoạt hoặc không tồn tại.
	public final static String CARD_PIN_FORMAT_INCORRECT_STATUS = "11";//Mã thẻ sai định dạng.
	public final static String CARD_SERIAL_INVALID_STATUS = "12";//Sai số serial của thẻ.
	public final static String CARD_SERIAL_AND_PIN_NOT_MATCH_STATUS = "13";//Mã thẻ và số serial không khớp.
	public final static String CARD_NOT_EXISTED_STATUS = "14";//"Thẻ không tồn tại"
	public final static String CARD_CAN_NOT_BE_USED_STATUS = "15";//"Thẻ không sử dụng được."

	public final static String NUMBER_FALSE_ENTRY_EXCEED_LIMIT_STATUS = "16";//"Số lần thử (nhập sai liên tiếp) của thẻ vượt quá giới hạn cho phép."
	public final static String TELCO_SYSTEMS_OVERLOADED_STATUS = "17";//Hệ thống đơn vị phát hành (Telco) bị lỗi hoặc quá tải, thẻ chưa bị trừ.
	public final static String TELCO_SYSTEMS_OVERLOADED_UNSURE_STATUS = "18";//Hệ thống đơn vị phát hành (Telco) bị lỗi hoặc quá tải, thẻ có thể bị trừ, cần phối hợp với 1pay.vn để tra soát.
	public final static String TELCO_NOT_EXISTED_STATUS = "19";//Đơn vị phát hành không tồn tại.
	public final static String TELCO_NOT_SUPPORT_BIZ_STATUS = "20";//Đơn vị phát hành không hỗ trợ nghiệp vụ này.

	public final static String CARD_TYPE_NOT_SUPPORTED_STATUS = "21"; // Không hỗ trợ loại card này.
	public final static String TELCO_CONNECTOR_CORRUPTED_STATUS = "22";//Kết nối tới hệ thống đơn vị phát hành (Telco) bị lỗi, thẻ chưa bị trừ (thường do lỗi kết nối với Telco, ví dụ sai tham số kết nối, mà không liên quan đến merchant)
	public final static String ONEPAY_CHARGE_ERROR_STATUS = "23";//Kết nối 1Pay tới hệ thống đơn vị cung cấp bị lỗi, thẻ chưa bị trừ.
	public final static String UNKNOWN_OR_UNDEFINE_ERROR_STATUS = "99";//"Lỗi, tuy nhiên lỗi chưa được định nghĩa hoặc chưa xác định được nguyên nhân."

	// --------
	public final static String SUCCESS_MESSAGE = "Giao dịch thành công.";
	public final static String INVALID_IP_MESSAGE = "Lỗi, địa chỉ IP truy cập API bị từ chối.";
	public final static String INVALID_INPUT_PARAMS_MESSAGE = "Lỗi, tham số gửi từ merchant tới chưa chính xác (thường sai tên tham số hoặc thiếu tham số).";
	public final static String MERCHANT_LOCKED_OR_NOT_EXISTED_MESSAGE = "Lỗi, merchant không tồn tại hoặc merchant đang bị khóa kết nối.";
	public final static String INVALID_PASSWORD_OR_SIGNATURE_MESSAGE = "Mật khẩu hoặc chữ ký xác thực không chính xác.";
	public final static String TRAN_REF_IS_DUPLICATED_MESSAGE = "Trùng mã giao dịch (transRef)";
	public final static String TRAN_REF_NOT_EXISTED_OR_INVALID_MESSAGE = "Mã giao dịch không tồn tại hoặc sai định dạng";

	public final static String CARD_IS_USED_OR_NOT_EXISTED_MESSAGE = "Thẻ đã được sử dụng, hoặc thẻ sai.";
	public final static String CARD_LOCKED_MESSAGE = "Thẻ bị khóa";
	public final static String CARD_EXPIRED_MESSAGE = "Thẻ hết hạn sử dụng.";
	public final static String CARD_NOT_ACTIVED_OR_NOT_EXISTED_MESSAGE = "Thẻ chưa được kích hoạt hoặc không tồn tại.";
	public final static String CARD_PIN_FORMAT_INCORRECT_MESSAGE = "Mã thẻ sai định dạng.";
	public final static String CARD_SERIAL_INVALID_MESSAGE = "Serial thẻ không đúng.";
	public final static String CARD_SERIAL_AND_PIN_NOT_MATCH_MESSAGE = "Mã thẻ và số serial không khớp.";
	public final static String CARD_NOT_EXISTED_MESSAGE = "Thẻ không tồn tại";
	public final static String CARD_CAN_NOT_BE_USED_MESSAGE = "Thẻ không sử dụng được.";

	public final static String NUMBER_FALSE_ENTRY_EXCEED_LIMIT_MESSAGE = "Số lần thử (nhập sai liên tiếp) của thẻ vượt quá giới hạn cho phép.";
	public final static String TELCO_SYSTEMS_OVERLOADED_MESSAGE = "Hệ thống đơn vị phát hành (Telco) bị lỗi hoặc quá tải, thẻ chưa bị trừ.";
	public final static String TELCO_SYSTEMS_OVERLOADED_UNSURE_MESSAGE = "Hệ thống đơn vị phát hành (Telco) bị lỗi hoặc quá tải, thẻ có thể bị trừ, cần phối hợp với 1pay.vn để tra soát.";
	public final static String TELCO_NOT_EXISTED_MESSAGE = "Đơn vị phát hành không tồn tại.";
	public final static String TELCO_NOT_SUPPORT_BIZ_MESSAGE = "Đơn vị phát hành không hỗ trợ nghiệp vụ này.";

	public final static String CARD_TYPE_NOT_SUPPORTED_MESSAGE="Không hỗ trợ loại card này.";
	public final static String TELCO_CONNECTOR_CORRUPTED_MESSAGE = "Kết nối tới hệ thống đơn vị phát hành (Telco) bị lỗi, thẻ chưa bị trừ (thường do lỗi kết nối với Telco, ví dụ sai tham số kết nối, mà không liên quan đến merchant).";
	public final static String ONEPAY_CHARGE_ERROR_MESSAGE = "Lỗi 1Pay kết nối thẻ tới hệ thống đơn vị cung cấp bị lỗi, thẻ chưa bị trừ.";
	public final static String UNKNOWN_OR_UNDEFINE_ERROR_MESSAGE = "Lỗi, tuy nhiên lỗi chưa được định nghĩa hoặc chưa xác định được nguyên nhân.";
	
	
	private static Map<String,String> MAP_STATUS_MESSAGE = new HashMap<String, String>();
	static{
		MAP_STATUS_MESSAGE.put(SUCCESS_STATUS, SUCCESS_MESSAGE);//00
		MAP_STATUS_MESSAGE.put(INVALID_IP_STATUS, INVALID_IP_MESSAGE);//01
		MAP_STATUS_MESSAGE.put(INVALID_INPUT_PARAMS_STATUS, INVALID_INPUT_PARAMS_MESSAGE);//02
		MAP_STATUS_MESSAGE.put(MERCHANT_LOCKED_OR_NOT_EXISTED_STATUS, MERCHANT_LOCKED_OR_NOT_EXISTED_MESSAGE);//03
		MAP_STATUS_MESSAGE.put(INVALID_PASSWORD_OR_SIGNATURE_STATUS, INVALID_PASSWORD_OR_SIGNATURE_MESSAGE);//04
		MAP_STATUS_MESSAGE.put(TRAN_REF_IS_DUPLICATED_STATUS, TRAN_REF_IS_DUPLICATED_MESSAGE);//05
		MAP_STATUS_MESSAGE.put(TRAN_REF_NOT_EXISTED_OR_INVALID_STATUS, TRAN_REF_NOT_EXISTED_OR_INVALID_MESSAGE);//06
		
		MAP_STATUS_MESSAGE.put(CARD_IS_USED_OR_NOT_EXISTED_STATUS, CARD_IS_USED_OR_NOT_EXISTED_MESSAGE);//07
		MAP_STATUS_MESSAGE.put(CARD_LOCKED_STATUS, CARD_LOCKED_MESSAGE);//08
		MAP_STATUS_MESSAGE.put(CARD_EXPIRED_STATUS, CARD_EXPIRED_MESSAGE);//09
		MAP_STATUS_MESSAGE.put(CARD_NOT_ACTIVED_OR_NOT_EXISTED_STATUS, CARD_NOT_ACTIVED_OR_NOT_EXISTED_MESSAGE);//10
		MAP_STATUS_MESSAGE.put(CARD_PIN_FORMAT_INCORRECT_STATUS, CARD_PIN_FORMAT_INCORRECT_MESSAGE);//11
		MAP_STATUS_MESSAGE.put(CARD_SERIAL_INVALID_STATUS, CARD_SERIAL_INVALID_MESSAGE);//12
		MAP_STATUS_MESSAGE.put(CARD_SERIAL_AND_PIN_NOT_MATCH_STATUS, CARD_SERIAL_AND_PIN_NOT_MATCH_MESSAGE);//13
		MAP_STATUS_MESSAGE.put(CARD_NOT_EXISTED_STATUS, CARD_NOT_EXISTED_MESSAGE);//14
		MAP_STATUS_MESSAGE.put(CARD_CAN_NOT_BE_USED_STATUS, CARD_CAN_NOT_BE_USED_MESSAGE);//15
		
		MAP_STATUS_MESSAGE.put(NUMBER_FALSE_ENTRY_EXCEED_LIMIT_STATUS, NUMBER_FALSE_ENTRY_EXCEED_LIMIT_MESSAGE);//16
		MAP_STATUS_MESSAGE.put(TELCO_SYSTEMS_OVERLOADED_STATUS, TELCO_SYSTEMS_OVERLOADED_MESSAGE);//17
		MAP_STATUS_MESSAGE.put(TELCO_SYSTEMS_OVERLOADED_UNSURE_STATUS, TELCO_SYSTEMS_OVERLOADED_UNSURE_MESSAGE);//18
		MAP_STATUS_MESSAGE.put(TELCO_NOT_EXISTED_STATUS, TELCO_NOT_EXISTED_MESSAGE);//19
		MAP_STATUS_MESSAGE.put(TELCO_NOT_SUPPORT_BIZ_STATUS, TELCO_NOT_SUPPORT_BIZ_MESSAGE);//20
		MAP_STATUS_MESSAGE.put(CARD_TYPE_NOT_SUPPORTED_STATUS, CARD_TYPE_NOT_SUPPORTED_MESSAGE);//21
		MAP_STATUS_MESSAGE.put(TELCO_CONNECTOR_CORRUPTED_STATUS, TELCO_CONNECTOR_CORRUPTED_MESSAGE);//22
		MAP_STATUS_MESSAGE.put(ONEPAY_CHARGE_ERROR_STATUS, ONEPAY_CHARGE_ERROR_MESSAGE);//23
		MAP_STATUS_MESSAGE.put(UNKNOWN_OR_UNDEFINE_ERROR_STATUS, UNKNOWN_OR_UNDEFINE_ERROR_MESSAGE);//99
	}
	public static String getMessage(String status){
		return MAP_STATUS_MESSAGE.containsKey(status) ? MAP_STATUS_MESSAGE.get(status) : null;
	}
	
	public final static List<String> ALL_CHARGING_ERROR_STATUS = Arrays
			.asList(new String[] { ChargeStatus.INVALID_IP_STATUS,
					ChargeStatus.INVALID_INPUT_PARAMS_STATUS,
					ChargeStatus.MERCHANT_LOCKED_OR_NOT_EXISTED_STATUS,
					ChargeStatus.INVALID_PASSWORD_OR_SIGNATURE_STATUS,
					ChargeStatus.TRAN_REF_IS_DUPLICATED_STATUS,
					ChargeStatus.TRAN_REF_NOT_EXISTED_OR_INVALID_STATUS,
					ChargeStatus.NUMBER_FALSE_ENTRY_EXCEED_LIMIT_STATUS,
					ChargeStatus.TELCO_SYSTEMS_OVERLOADED_STATUS,
					ChargeStatus.TELCO_SYSTEMS_OVERLOADED_UNSURE_STATUS,
					ChargeStatus.TELCO_NOT_EXISTED_STATUS,
					ChargeStatus.TELCO_NOT_SUPPORT_BIZ_STATUS,
					ChargeStatus.CARD_TYPE_NOT_SUPPORTED_STATUS,
					ChargeStatus.TELCO_CONNECTOR_CORRUPTED_STATUS,
					ChargeStatus.UNKNOWN_OR_UNDEFINE_ERROR_STATUS,
					ChargeStatus.ONEPAY_CHARGE_ERROR_STATUS, });
	
	public final static List<String> ALL_CHARGING_WRONG_STATUS = Arrays
			.asList(new String[] { 
					ChargeStatus.CARD_IS_USED_OR_NOT_EXISTED_STATUS,
					ChargeStatus.CARD_LOCKED_STATUS,
					ChargeStatus.CARD_EXPIRED_STATUS,
					ChargeStatus.CARD_NOT_ACTIVED_OR_NOT_EXISTED_STATUS,
					ChargeStatus.CARD_PIN_FORMAT_INCORRECT_STATUS,
					ChargeStatus.CARD_SERIAL_INVALID_STATUS,
					ChargeStatus.CARD_SERIAL_AND_PIN_NOT_MATCH_STATUS,
					ChargeStatus.CARD_NOT_EXISTED_STATUS,
					ChargeStatus.CARD_CAN_NOT_BE_USED_STATUS,
					});
	
	public final static List<String> ALL_SYSTEM_ERROR_STATUS = Arrays
			.asList(new String[] { 
					ChargeStatus.INVALID_IP_STATUS,
					ChargeStatus.INVALID_INPUT_PARAMS_STATUS,
					ChargeStatus.MERCHANT_LOCKED_OR_NOT_EXISTED_STATUS,
//					ChargeStatus.INVALID_PASSWORD_OR_SIGNATURE_STATUS,
					ChargeStatus.NUMBER_FALSE_ENTRY_EXCEED_LIMIT_STATUS,
					ChargeStatus.TELCO_SYSTEMS_OVERLOADED_STATUS,
					ChargeStatus.TELCO_SYSTEMS_OVERLOADED_UNSURE_STATUS,
					ChargeStatus.TELCO_NOT_EXISTED_STATUS,
					ChargeStatus.TELCO_NOT_SUPPORT_BIZ_STATUS,
					ChargeStatus.TELCO_CONNECTOR_CORRUPTED_STATUS,
					ChargeStatus.ONEPAY_CHARGE_ERROR_STATUS, 
					});
	
	public final static List<String> FUZZY_STATUS = Arrays.asList(new String[]{
			ChargeStatus.TELCO_SYSTEMS_OVERLOADED_STATUS,
			ChargeStatus.TELCO_SYSTEMS_OVERLOADED_UNSURE_STATUS,
	});
}
