package com.example.FinCore.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 家庭群組，由1個或多個User組成，
 * 只會有1名owner跟0~多個invitor
 */
@Entity
@Table(name = "family")
public class Family {
    
	//id是流水號
    @Id
    @Column(name = "id")
    private int id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "owner")
    private String owner;
    
    @Column(name = "invitor")
    private String invitor;
    
    @Column(name = "create_date")
    private LocalDate createDate;
    
    private final transient ObjectMapper mapper = new ObjectMapper(); 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

	public String getInvitor() {
		return invitor;
	}

	public void setInvitor(String invitor) {
		this.invitor = invitor;
	}

	public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

	@Override
	public String toString() {
		return "Family [id=" + id + ", name=" + name + ", owner=" + owner + ", invitor=" + invitor + ", createDate="
				+ createDate + "]";
	}
	
	/**
	 * 將 invitor 屬性轉回成員列表並返回。
	 * @return 成員列表
	 */
	public List<String> toMemberList()
	{
		try 
		{
			return mapper.readValue(invitor, new TypeReference<List<String>>() {});
		} 
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 將成員列表轉換並回存 invitor 屬性。
	 * @param memberList 成員列表
	 */
	public void toInvitor(List<String> memberList)
	{
		try 
		{
			invitor = mapper.writeValueAsString(memberList);
		} 
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 檢查指定帳號是否為群組成員的一份子。
	 * @param account 指定帳號
	 * @return 如果該帳號為群組管理者或存在於成員名單中就返回 {@code TRUE}
	 */
	public boolean isMember(String account)
	{
		List<String> memberList = toMemberList();
		if(owner.equals(account) || memberList.contains(account))
			return true;
		
		return false;
	}
	

}