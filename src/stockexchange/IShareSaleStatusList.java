package stockexchange;

import client.Customer;

/**
 * Created by gay.hazan on 22/05/2015.
 */
public interface IShareSaleStatusList {

    ShareSalesStatusList sellShares(ShareList shareItemList, Customer info);
}
