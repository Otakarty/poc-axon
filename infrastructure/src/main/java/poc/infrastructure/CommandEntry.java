package poc.infrastructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity(name = "COMMAND")
public class CommandEntry {
    @Id
    @Column(name = "COMMAND_ID")
    private String commandId;

    @Column(name = "COMMAND_NAME")
    private String commandName;

    // @Column(name = "DT_CREATION")
    // private LocalDateTime dtCreation;
    //
    // @Column(name = "SOURCE")
    // private String source;

    @Column(name = "AGGREGATE_ID")
    private String aggregateId;

    @Column(name = "AGGREGATE_TYPE")
    private String aggregateType;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DETAIL")
    private String detail;

    @Lob
    @Column(name = "COMMAND")
    private byte[] commmand;

    public String getCommandId() {
        return this.commandId;
    }

    public void setCommandId(final String myCommandId) {
        this.commandId = myCommandId;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public void setCommandName(final String myCommandName) {
        this.commandName = myCommandName;
    }

    public String getAggregateId() {
        return this.aggregateId;
    }

    public void setAggregateId(final String myAggregateId) {
        this.aggregateId = myAggregateId;
    }

    public String getAggregateType() {
        return this.aggregateType;
    }

    public void setAggregateType(final String myAggregateType) {
        this.aggregateType = myAggregateType;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(final String myOrderId) {
        this.orderId = myOrderId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String myStatus) {
        this.status = myStatus;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(final String myDetail) {
        this.detail = myDetail;
    }

    public byte[] getCommmand() {
        return this.commmand;
    }

    public void setCommmand(final byte[] myCommmand) {
        this.commmand = myCommmand;
    }

}
