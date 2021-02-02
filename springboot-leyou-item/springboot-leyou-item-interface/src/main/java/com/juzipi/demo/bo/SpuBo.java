package com.juzipi.demo.bo;

import com.juzipi.demo.pojo.Sku;
import com.juzipi.demo.pojo.Spu;
import com.juzipi.demo.pojo.SpuDetail;
import lombok.Data;

import java.util.List;

@Data
public class SpuBo extends Spu {
    private String cname;
    private String bname;

    private SpuDetail spuDetail;

    private List<Sku> skus;
}
