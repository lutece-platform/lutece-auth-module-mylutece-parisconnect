package fr.paris.lutece.plugins.mylutece.modules.parisconnect.web;

import java.io.Serializable;

/**
 * 
 * ParisConnectResponse
 *
 */
public class ParisConnectResponse implements Serializable {


	private static final long serialVersionUID = 5100059838966010325L;
	private String _strToken;
	private Boolean _bSuccess;
	
	
	
	public ParisConnectResponse(Boolean bSuccess,String strToken)
	{
		
		_strToken=strToken;
		_bSuccess=bSuccess;
	}
	
	/**
	 * 
	 * @return get token in json response
	 */
	public String getToken() {
		return _strToken;
	}
	/**
	 * set new token in json response
	 * @param _strToken set token in json response
	 */
	public void setToken(String _strToken) {
		this._strToken = _strToken;
	}
	
	/**
	 * true if  success response false
	 * @return true if  success response false
	 */ 
	public Boolean getSuccess() {
		return _bSuccess;
	}
	
	/**
	 * true if  success response 
	 * @return true if  success response 
	 */
	public void setSuccess(Boolean _bSuccess) {
		this._bSuccess = _bSuccess;
	}

	

}
