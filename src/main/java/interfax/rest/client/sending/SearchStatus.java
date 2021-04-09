package interfax.rest.client.sending;

/**
 * status.
 * [Sending Faxes][Search Fax List] 呼び出し時に
 * status に対して定義可能な選択肢の定義
 */
public enum SearchStatus {
    
    ////////////////////////////////////////////////////////////////////////////
    // enum(s)
    
    All,
    Completed,
    Success,
    Failed,
    Inprocess;
}
