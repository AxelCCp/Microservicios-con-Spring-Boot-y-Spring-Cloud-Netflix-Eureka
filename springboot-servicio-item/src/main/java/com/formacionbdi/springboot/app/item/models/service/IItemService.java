package com.formacionbdi.springboot.app.item.models.service;

import java.util.List;

import com.formacionbdi.springboot.app.item.models.Item;

public interface IItemService {
	public List<Item>findAll();
	public Item findById(Long id, Integer cantidad);
}
