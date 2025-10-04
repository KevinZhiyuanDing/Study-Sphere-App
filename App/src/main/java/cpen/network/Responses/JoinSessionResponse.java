package cpen.network.Responses;

import org.json.JSONObject;

import cpen.ADT.User;

public class JoinSessionResponse implements Response {
    private final ResponseType responseType = ResponseType.JOIN_SESSION;
    private boolean processed;

    public JoinSessionResponse(String str) {
        JSONObject json = new JSONObject(str);

        this.processed = json.getBoolean("processed");
    }

    public boolean isJoinProcessed() {
        return processed;
    }

    @Override
    public ResponseType getThisResponseType() {
        return responseType;
    }
}