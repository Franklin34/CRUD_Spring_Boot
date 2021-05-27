package mx.com.gm.web;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.com.gm.domain.Persona;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import mx.com.gm.servicio.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@Slf4j
public class ControladorInicio {

    @Autowired //Para inyectar la dependencia
    private PersonaService personaService;
    
    @GetMapping("/")
    public String inicio(Model model,@AuthenticationPrincipal User user){
        var personas = personaService.listarPersonas();
        log.info("usuario que hizo login: " +  user);
        log.info("Ejecutando el controlador tipo Spring MVC");
        model.addAttribute("personas",personas);
        var saldoTotal = 0D;
        
        for(var p:personas){
            saldoTotal += p.getSaldo();
        }
        
        model.addAttribute("saldoTotal", saldoTotal);
        model.addAttribute("totalClientes", personas.size());
        return "index";
    }
    
    @GetMapping("/agregar")
    public String agregar(Persona persona){
        return "modificar";
    }
    
    @PostMapping("/guardar")
    public String guardar(@Valid Persona persona,Errors errores){
        
        if(errores.hasErrors()){
            return "modificar";
        }
        
        personaService.guardar(persona);
        return "redirect:/";
    }
    
    @GetMapping("/editar/{idPersona}")
    public String editar(Persona persona,Model model){
        persona = personaService.encontrarPersona(persona);
        model.addAttribute("persona",persona);
        return "modificar";
    }
    
    @GetMapping("/eliminar")
    public String eliminar(Persona persona){
        personaService.eliminar(persona);
        return "redirect:/";
    }
}