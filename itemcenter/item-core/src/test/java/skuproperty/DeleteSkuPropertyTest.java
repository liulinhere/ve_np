package skuproperty;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ve.itemcenter.common.api.BaseRequest;
import com.ve.itemcenter.common.api.ItemService;
import com.ve.itemcenter.common.api.Request;
import com.ve.itemcenter.common.api.Response;
import com.ve.itemcenter.common.constant.ActionEnum;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class DeleteSkuPropertyTest {
	@Resource
	private ItemService itemService;

	@Test
	/**
	 * 正常删除
	 */
	public void test001() {
		Request request = new BaseRequest();
		request.setParam("ID", 84L);
		request.setParam("sellerId", 1L);
		request.setCommand(ActionEnum.DELETE_SKU_PROPERTY.getActionName());
		Response response = itemService.execute(request);
		System.out.println("**************************************");
		System.out.println("Model:" + response.getModule());
		System.out.println("message:" + response.getMessage());
		System.out.println("ErrorCode:" + response.getErrorCode());
		System.out.println("TotalCount:" + response.getTotalCount());
		System.out.println("**************************************");
	}

	@Test
	/**
	 * ID为空
	 */
	public void test002() {
		Request request = new BaseRequest();
		request.setCommand(ActionEnum.DELETE_SKU_PROPERTY.getActionName());
		Response response = itemService.execute(request);
		System.out.println("**************************************");
		System.out.println("Model:" + response.getModule());
		System.out.println("message:" + response.getMessage());
		System.out.println("ErrorCode:" + response.getErrorCode());
		System.out.println("TotalCount:" + response.getTotalCount());
		System.out.println("**************************************");
	}

	@Test
	/**
	 * sellerId为空
	 */
	public void test003() {
		Request request = new BaseRequest();
		request.setParam("ID", 84L);
		request.setCommand(ActionEnum.DELETE_SKU_PROPERTY.getActionName());
		Response response = itemService.execute(request);
		System.out.println("**************************************");
		System.out.println("Model:" + response.getModule());
		System.out.println("message:" + response.getMessage());
		System.out.println("ErrorCode:" + response.getErrorCode());
		System.out.println("TotalCount:" + response.getTotalCount());
		System.out.println("**************************************");
	}

	@Test
	/**
	 * ID不存在
	 */
	public void test004() {
		Request request = new BaseRequest();
		request.setParam("ID", 999999L);
		request.setParam("sellerId", 1L);
		request.setCommand(ActionEnum.DELETE_SKU_PROPERTY.getActionName());
		Response response = itemService.execute(request);
		System.out.println("**************************************");
		System.out.println("Model:" + response.getModule());
		System.out.println("message:" + response.getMessage());
		System.out.println("ErrorCode:" + response.getErrorCode());
		System.out.println("TotalCount:" + response.getTotalCount());
		System.out.println("**************************************");
	}
	
//	@Test
//	/**
//	 * 根据ID和sellerID查找到的记录中的skuId在数据库中不存在
//	 */
//	public void test005() {
//		Request request = new BaseRequest();
//		request.setParam("ID", 100L);
//		request.setParam("sellerId", 1L);
//		request.setCommand(ActionEnum.DELETE_SKU_PROPERTY.getActionName());
//		Response response = itemService.execute(request);
//		System.out.println("**************************************");
//		System.out.println("Model:" + response.getModule());
//		System.out.println("message:" + response.getMessage());
//		System.out.println("ErrorCode:" + response.getErrorCode());
//		System.out.println("TotalCount:" + response.getTotalCount());
//		System.out.println("**************************************");
//	}
}