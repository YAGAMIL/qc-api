package com.quantumtime.qc.service;

public interface IMQService {
    boolean sendQualify(String json);

    boolean sendFeeds(String json);
}
