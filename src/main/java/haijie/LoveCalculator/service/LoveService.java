package haijie.LoveCalculator.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import haijie.LoveCalculator.models.LoveProperties;

@Qualifier("loveRedis")
@Service
public class LoveService {
    private static final String LOVE_CAL_URL=
    "https://love-calculator.p.rapidapi.com/getPercentage";
    
    //redis
    private static final String CONTACT_ENTITY = "lovelist";
    
    @Autowired
    RedisTemplate<String,Object> redisTemplate;


    public Optional<LoveProperties> getLovePercent(String fname, String sname) throws IOException, InterruptedException{
        String apiKey = System.getenv("X-RapidAPI-Key"); //get the api key
        String loveURI=UriComponentsBuilder
        .fromUriString(LOVE_CAL_URL)
        .queryParam("sname",sname)
        .queryParam("fname",fname)
        .toUriString();
        
        HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create(loveURI))
		.header("X-RapidAPI-Key", apiKey)
		.header("X-RapidAPI-Host", "love-calculator.p.rapidapi.com")
		.method("GET", HttpRequest.BodyPublishers.noBody())
		.build();
        
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        LoveProperties l = LoveProperties.create(response.body());
        if(l != null){
            save(l);
            return Optional.of(l);
        }                        
        return Optional.empty();
        
    }
    
    //redis
    public void save(final LoveProperties lpt){
        redisTemplate.opsForList().leftPush(CONTACT_ENTITY, lpt.getId());
        redisTemplate.opsForHash().put(CONTACT_ENTITY+"_Map", lpt.getId(), lpt);

    }

    public LoveProperties findById(final String contactId){
        LoveProperties result = (LoveProperties)redisTemplate.opsForHash().get(CONTACT_ENTITY+"_Map",contactId);

        return result;
    }

    public List<LoveProperties> findAll(){
        List<Object> fromLoveList = redisTemplate.opsForList().range(CONTACT_ENTITY,0,-1);
    
        List<LoveProperties> ctcs = redisTemplate
        .opsForHash().multiGet(CONTACT_ENTITY+"_Map", fromLoveList)
        .stream().filter(LoveProperties.class::isInstance)
        .map(LoveProperties.class::cast).toList();
        //the filter line above has the same meaning as -> if(a instanceof Contact)

        return ctcs;
    }
}