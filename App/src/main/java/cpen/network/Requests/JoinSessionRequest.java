package cpen.network.Requests;

import org.json.JSONObject;

import cpen.adt.*;

public class JoinSessionRequest implements Request {
    private RequestType requestType = RequestType.JOIN_SESSION;
    private User requester;
    private Session session;

    public JoinSessionRequest(User requester, Session session) {
        this.requester = requester;
        this.session = session;
    }

    @Override
    public String string() {
        JSONObject json = new JSONObject();

        json.put("requestUser", requester.getUsername());
        json.put("requestPass", requester.getPassword());

        json.put("hostUser", session.getHost().getUsername());
        json.put("hostPass", session.getHost().getPassword());

        json.put("course", session.getCourse());

        json.put("building", session.getBuilding());
        json.put("roomNum", session.getRoom());
        json.put("startDate", session.getStartTime().getDateTimeString(DateTimeFormat.YYYY_MM_DD));
        json.put("endDate", session.getEndTime().getDateTimeString(DateTimeFormat.YYYY_MM_DD));
        json.put("sessionDesc", session.getDescription());

        return json.toString();
    }

    @Override
    public RequestType getRequestType() {
        return this.requestType;
    }
}