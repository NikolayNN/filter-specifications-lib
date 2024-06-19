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
 <tr><td>nn     </td><td> not null                       </td><td>firstName=nn#null     </td></tr>
 </table>

<p>Examples of path strings:</p>
    <ul>
       <li><b>Simple attribute:</b> "username"
           <p>Direct attribute of the root entity.</p>
           <p><code>root.get("username")</code></p>
       </li>
       <li><b>Nested attribute:</b> "user.address.street"
           <p>Navigates through nested entities: from 'user' to 'address', then to 'street'.</p>
           <p><code>root.get("user").get("address").get("street")</code></p>
       </li>
       <li><b>Collection elements:</b> "$employees.name"
           <p>Accesses elements of a collection 'employees' and retrieves the 'name' attribute.</p>
           <p><code>root.join("employees").get("name")</code></p>
       </li>
       <li><b>Polymorphic type casting:</b> "user.t:com.example.SpecialUser.specialAttribute"
           <p>Casts the 'user' path to 'SpecialUser' to access a specific attribute 'specialAttribute' available only in the subclass.</p>
           <p><code>criteriaBuilder.treat(root.get("user"), SpecialUser.class).get("specialAttribute")</code></p>
       </li>
       <li><b>Polymorphic type casting for collection elements:</b> "$employees.t:com.example.SpecialEmployee.specialSkill"
            <p>Accesses a collection 'employees', and casts each element to 'SpecialEmployee' to retrieve a specific attribute 'specialSkill' available only in the subclass.</p>
            <p><code>criteriaBuilder.treat(root.join("employees"), SpecialEmployee.class).get("specialSkill")</code></p>
        </li>
    </ul>


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
