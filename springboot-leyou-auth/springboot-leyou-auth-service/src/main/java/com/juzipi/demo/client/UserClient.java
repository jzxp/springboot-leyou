package com.juzipi.demo.client;

import com.juzipi.demo.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient("user-service")
public interface UserClient extends UserApi {
}
