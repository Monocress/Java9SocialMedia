package com.dorukt.rabbitmq.model;

import com.dorukt.repository.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data //@Getter @Setter @ToString hepsini kapsıyor.
@Builder
@Deprecated
public class ActivateModel implements Serializable {

    Long authId;
    EStatus status;
}
