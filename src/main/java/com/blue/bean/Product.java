package com.blue.bean;

import java.io.Serializable;

/**
 * 因为product要放到购物车hashmap中作为主键，所以需要重写hashcode和equals方法
 * */
public class Product implements Serializable{

	private static final long serialVersionUID = 7357412112773898204L;

	private String id;

    private String name;

    private Double price;

    private String category;

    private Integer pnum;

    private String imgurl;

    private String description;

    // 在获取销售榜单信息时使用
 	private int totalSaleNum; // 总销售数量
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public Integer getPnum() {
        return pnum;
    }

    public void setPnum(Integer pnum) {
        this.pnum = pnum;
    }

    public String getImgurl() {
        return imgurl;
    }
    
  //根据imgurl得到缩略图图片路径
  	public String getImgurl_s() {
  		int index = imgurl.lastIndexOf(".");
  		String filename = imgurl.substring(0, index);
  		String ext = imgurl.substring(index);

  		return filename + "_s" + ext;
  	}

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public int getTotalSaleNum() {
		return totalSaleNum;
	}

	public void setTotalSaleNum(int totalSaleNum) {
		this.totalSaleNum = totalSaleNum;
	}

	/**
	 * 重写hashCode和equals方法，因为Product对象要放到hashMap中
	 * */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}