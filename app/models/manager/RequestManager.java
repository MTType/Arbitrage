

package models.manager;

import java.util.ArrayList;
import java.util.List;
import models.entity.Exchange;
import models.entity.Request;
import play.db.jpa.Transactional;


public class RequestManager {
    
    @Transactional
    public static List<Request> getExchangeRequests(Exchange exchange) {
        List<Request> exchangeRequestList = new ArrayList<Request>();
        List<Request> requestList = Request.findAll();
        for (Request request: requestList ) {
            if (request.exchange == exchange) {
                exchangeRequestList.add(request);
            }
        }
        return exchangeRequestList;
    }
}
