
package business.WSClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the business package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetTickerResponse_QNAME = new QName("http://business/", "getTickerResponse");
    private final static QName _RecievePaymentResponse_QNAME = new QName("http://business/", "recievePaymentResponse");
    private final static QName _RecievePayment_QNAME = new QName("http://business/", "recievePayment");
    private final static QName _GetTicker_QNAME = new QName("http://business/", "getTicker");
    private final static QName _IssueShares_QNAME = new QName("http://business/", "issueShares");
    private final static QName _IssueSharesResponse_QNAME = new QName("http://business/", "issueSharesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: business
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetTickerResponse }
     * 
     */
    public GetTickerResponse createGetTickerResponse() {
        return new GetTickerResponse();
    }

    /**
     * Create an instance of {@link RecievePaymentResponse }
     * 
     */
    public RecievePaymentResponse createRecievePaymentResponse() {
        return new RecievePaymentResponse();
    }

    /**
     * Create an instance of {@link IssueSharesResponse }
     * 
     */
    public IssueSharesResponse createIssueSharesResponse() {
        return new IssueSharesResponse();
    }

    /**
     * Create an instance of {@link IssueShares }
     * 
     */
    public IssueShares createIssueShares() {
        return new IssueShares();
    }

    /**
     * Create an instance of {@link RecievePayment }
     * 
     */
    public RecievePayment createRecievePayment() {
        return new RecievePayment();
    }

    /**
     * Create an instance of {@link GetTicker }
     * 
     */
    public GetTicker createGetTicker() {
        return new GetTicker();
    }

    /**
     * Create an instance of {@link ShareOrder }
     * 
     */
    public ShareOrder createShareOrder() {
        return new ShareOrder();
    }

    /**
     * Create an instance of {@link Share }
     * 
     */
    public Share createShare() {
        return new Share();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTickerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://business/", name = "getTickerResponse")
    public JAXBElement<GetTickerResponse> createGetTickerResponse(GetTickerResponse value) {
        return new JAXBElement<GetTickerResponse>(_GetTickerResponse_QNAME, GetTickerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecievePaymentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://business/", name = "recievePaymentResponse")
    public JAXBElement<RecievePaymentResponse> createRecievePaymentResponse(RecievePaymentResponse value) {
        return new JAXBElement<RecievePaymentResponse>(_RecievePaymentResponse_QNAME, RecievePaymentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RecievePayment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://business/", name = "recievePayment")
    public JAXBElement<RecievePayment> createRecievePayment(RecievePayment value) {
        return new JAXBElement<RecievePayment>(_RecievePayment_QNAME, RecievePayment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTicker }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://business/", name = "getTicker")
    public JAXBElement<GetTicker> createGetTicker(GetTicker value) {
        return new JAXBElement<GetTicker>(_GetTicker_QNAME, GetTicker.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IssueShares }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://business/", name = "issueShares")
    public JAXBElement<IssueShares> createIssueShares(IssueShares value) {
        return new JAXBElement<IssueShares>(_IssueShares_QNAME, IssueShares.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IssueSharesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://business/", name = "issueSharesResponse")
    public JAXBElement<IssueSharesResponse> createIssueSharesResponse(IssueSharesResponse value) {
        return new JAXBElement<IssueSharesResponse>(_IssueSharesResponse_QNAME, IssueSharesResponse.class, null, value);
    }

}
