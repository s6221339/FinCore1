package com.example.FinCore.service.itfc;

import com.example.FinCore.vo.request.CreateFamilyRequest;
import com.example.FinCore.vo.request.DeleteRequest;
import com.example.FinCore.vo.request.UpdateRequest;
import com.example.FinCore.vo.response.BasicResponse;


public interface FamilyService {
	
	public BasicResponse create(CreateFamilyRequest req);
	
	public BasicResponse update(UpdateRequest req);
	
	public BasicResponse delete(DeleteRequest req);
}
