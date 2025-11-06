package com.example.pboproject.beans;

public class PointsFlow {
    private int pointFlowId;
    private int totalPoints;
    private int pointAdd;
    private int pointSubtract;

    public PointsFlow(int pointFlowId, int totalPoints, int pointAdd, int pointSubtract) {
        this.pointFlowId = pointFlowId;
        this.totalPoints = totalPoints;
        this.pointAdd = pointAdd;
        this.pointSubtract = pointSubtract;
    }
}
