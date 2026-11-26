package com.smartithelpdesk.smartithelpdesk;

public class SupportTicket {

    // Private attributes
    private String ticketId;
    private String assetId;
    private String issue;
    private String status;

    // Constructor
    public SupportTicket(String ticketId, String assetId,
            String issue, String status) {

        setTicketId(ticketId);
        setAssetId(assetId);
        setIssue(issue);
        setStatus(status);
    }

    // Getters
    public String getTicketId() {
        return ticketId;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getIssue() {
        return issue;
    }

    public String getStatus() {
        return status;
    }

    // Setters with validation
    public void setTicketId(String ticketId) {
        if (ticketId == null || ticketId.isBlank()) {
            throw new IllegalArgumentException(
                    "Ticket ID cannot be blank"
            );
        }
        this.ticketId = ticketId;
    }

    public void setAssetId(String assetId) {
        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException(
                    "Asset ID cannot be blank"
            );
        }
        this.assetId = assetId;
    }

    public void setIssue(String issue) {
        if (issue == null || issue.isBlank()) {
            throw new IllegalArgumentException(
                    "Issue cannot be blank"
            );
        }
        this.issue = issue;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "Status cannot be blank"
            );
        }
        this.status = status;
    }
}
