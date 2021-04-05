*** get started 
1.
```
public class ConvertersExt extends Converters {

    @Override
    public void addConverters() {
        map.put(NotificationType.class, NotificationType::valueOf);
        map.put(ProviderType.class, ProviderType::valueOf);
        map.put(CommandType.class, CommandType::valueOf);
    }
}
```
2. 
```
@ComponentScan(basePackages = {"by.nhorushko.filterspecification"})
```

 <table border="1">
 <tr><td> Symbol   </td><td> Operation                   </td><td>Example filter query param</td>
 <tr><td>eq       </td><td> Equals                     </td><td>city=eq#Sydney	         </td>
 <tr><td>neq      </td><td> Not Equals                 </td><td>country=neq#uk          </td>
 <tr><td>gt       </td><td> Greater Than               </td><td>amount=gt#10000         </td>
 <tr><td>gte      </td><td> Greater Than or equals to  </td><td>amount=gte#10000        </td>
 <tr><td>lt       </td><td> Less Than                  </td><td>amount=lt#10000         </td>
 <tr><td>lte      </td><td> Less Than or equals to     </td><td>amount=lte#10000        </td>
 <tr><td>in       </td><td> IN                         </td><td>country=in#uk, usa, au  </td>
 <tr><td>nin      </td><td> Not IN                     </td><td>country=nin#fr, de, nz  </td>
 <tr><td>btn      </td><td> Between                    </td><td>joiningDate=btn#2018-01-01, 2016-01-01   </td>
 <tr><td>like     </td><td> Like                       </td><td>firstName=like#John     </td></tr>
 <tr><td>en     </td><td> equalsNull                       </td><td>firstName=en#null     </td></tr>
 </table>


Подключение библиотеки:
```
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>

       <dependency>
            <groupId>com.github.NikolayNN</groupId>
            <artifactId>filter-specifications-lib</artifactId>
            <version>2.0</version>
        </dependency>
```
