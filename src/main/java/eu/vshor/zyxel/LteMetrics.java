package eu.vshor.zyxel;

public class LteMetrics {
  public long timestamp = System.currentTimeMillis();
  public Long cellid;
  public Long rssiscc;
  public Long rsrpscc;
  public Long rsrqscc;
  public Long sinrscc;
  public Long earfcnscc;
  public Long rsrp;
  public Long rsrq;
  public Long sinr;
  public String antenna;

  public Double getRssiscc(Number ignored) {
    return rssiscc.doubleValue();
  }

  public Double getRsrpscc(Number ignored) {
    return rsrpscc.doubleValue();
  }

  public Double getRsrqscc(Number ignored) {
    return rsrqscc.doubleValue();
  }

  public Double getSinrscc(Number ignored) {
    return sinrscc.doubleValue();
  }

  public Double getEarfcnscc(Number ignored) {
    return earfcnscc.doubleValue();
  }

  public Double getRsrp(Number ignored) {
    return rsrp.doubleValue();
  }

  public Double getRsrq(Number ignored) {
    return rsrq.doubleValue();
  }

  public Double getSinr(Number ignored) {
    return sinr.doubleValue();
  }

  @Override
  public String toString() {
    return "ReallyInterestingData{" +
        "timestamp=" + timestamp +
        ", cellid=" + cellid +
        ", rssiscc=" + rssiscc +
        ", rsrpscc=" + rsrpscc +
        ", rsrqscc=" + rsrqscc +
        ", sinrscc=" + sinrscc +
        ", earfcnscc=" + earfcnscc +
        ", rsrp=" + rsrp +
        ", rsrq=" + rsrq +
        ", sinr=" + sinr +
        ", antenna=" + antenna +
        '}';
  }

  public void update(LteMetrics that) {
    this.antenna = that.antenna;
    this.cellid = that.cellid;
    this.rssiscc = that.rssiscc;
    this.rsrpscc = that.rsrpscc;
    this.rsrqscc = that.rsrqscc;
    this.sinrscc = that.sinrscc;
    this.earfcnscc = that.earfcnscc;
    this.rsrp = that.rsrp;
    this.rsrq = that.rsrq;
    this.sinr = that.sinr;
  }

  public void reset() {
    this.antenna = null;
    this.cellid = null;
    this.rssiscc = null;
    this.rsrpscc = null;
    this.rsrqscc = null;
    this.sinrscc = null;
    this.earfcnscc = null;
    this.rsrp = null;
    this.rsrq = null;
    this.sinr = null;
  }
}
