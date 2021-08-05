package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "/basic/addForm";
    }

    // html Form name 값으로 넘어옴
    // @PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "/basic/item";
    }

    //@PostMapping("/add")
    // @ModelAttribute에 이름 넣는 것 => 지정해준 이름으로 model에 자동으로 넣어줌
    // 파라미터에 Model까지 날려도 된다..
    public String addItemV2 (@ModelAttribute("item") Item item) {

        itemRepository.save(item);
//        model.addAttribute("item", item); //자동 추가, 생략 가능

        return "/basic/item";
    }
    // @PostMapping("/add")
    // @ModelAttribute 네이밍 룰 => 객체의 이름 중 첫글자만 소문자로 바꿔줌
    // 이름 따로 넣지 않으면 된다.
    // HelloData -> helloData
    // Item -> item
    public String addItemV3 (@ModelAttribute Item item) {
        itemRepository.save(item);
        return "/basic/item";
    }

    // @PostMapping("/add")
    // @ModelAttribute 심지어 생략까지 됨..
    // 모델에 담으면서 이름도 자동 지정해주면서.. 객체에 자동 매칭까지 완벽
    public String addItemV4 (Item item) {
        itemRepository.save(item);
        return "/basic/item";
    }

//    @PostMapping("/add")
    // PRG 패턴
    public String addItemV5 (Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    @PostMapping("/add")
    public String addItemV6 (Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "/basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        // redirect 하면 @PathVariable에 있는 값을 사용할 수 있음.
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }
}
