package template.application.port;

import template.application.usecase.GetItem;
import template.application.usecase.GetItems;
import template.application.usecase.PutItem;

public interface ItemsWebPort extends GetItem, GetItems, PutItem {

}
