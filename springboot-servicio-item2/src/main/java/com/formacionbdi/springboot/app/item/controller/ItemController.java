package com.formacionbdi.springboot.app.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.service.IItemService;

@RestController
public class ItemController {

	@GetMapping("/listar")
	public List<Item> listar() {
		return itemService.findAll();
	}

	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable(value = "id") Long id, @PathVariable(value = "cantidad") Integer cantidad) {
		return itemService.findById(id, cantidad);
	}

	@Autowired
	@Qualifier("serviceFeign")
	// @Qualifier("serviceRestTemplate")
	private IItemService itemService;
}
