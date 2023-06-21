package com.reece.platform.eclipse.controller;

import com.reece.platform.eclipse.exceptions.EclipseTokenException;
import com.reece.platform.eclipse.model.DTO.ErrorDTO;
import com.reece.platform.eclipse.model.generated.Territory;
import com.reece.platform.eclipse.service.EclipseService.EclipseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/territories")
public class TerritoriesController {

    @Autowired
    private EclipseService eclipseService;

    @Autowired
    public TerritoriesController(EclipseService eclipseService) { this.eclipseService = eclipseService; }

    /**
     * Fetch a territory by id
     * @param territoryId
     * @return
     * @throws EclipseTokenException
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{territoryId}")
    public @ResponseBody Territory getTerritoryById(
            @PathVariable String territoryId
    ) throws EclipseTokenException {
        return eclipseService.getTerritoryById(territoryId);
    }

}
