package com.example.FinCore.vo.response;

import java.util.List;

import com.example.FinCore.constants.ResponseMessages;
import com.example.FinCore.vo.TransfersVO;

public class TransfersListResponse extends BasicResponse 
{
	
	private List<TransfersVO> transfersList;
	
	public TransfersListResponse() {
		super();
	}

	public TransfersListResponse(int code, String message) {
		super(code, message);
	}
	
	public TransfersListResponse(int code, String message, List<TransfersVO> transfersList) {
		super(code, message);
		this.transfersList = transfersList;
	}

	public TransfersListResponse(ResponseMessages res) {
		super(res);
	}
	
	public TransfersListResponse(ResponseMessages res, List<TransfersVO> transfersList) {
		super(res);
		this.transfersList = transfersList;
	}

	public List<TransfersVO> getTransfersList() {
		return transfersList;
	}
	
}
