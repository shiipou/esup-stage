/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.esupportail.esupstage.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author vagrant
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Account {
    private String displayName;
    private List<String> roles;
    private Boolean switched;

    public Account(String displayName, List<String> roles, boolean switched) {
        this.displayName = displayName;
        this.roles = roles;
        this.switched = null;
        if(switched) {
            this.switched = switched;
        }
    }
}
