package template.application.port;

import template.application.usecase.CreateItem;
import template.application.usecase.GetItem;
import template.application.usecase.GetItems;
import template.application.usecase.InsertItem;

public interface ItemsWebPort extends GetItem, GetItems, CreateItem, InsertItem {

}
