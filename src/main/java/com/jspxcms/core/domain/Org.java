package com.jspxcms.core.domain;

import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Org
 *
 * @author liufang
 */
@Entity
@Table(name = "cms_org")
public class Org implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 树编码长度
     */
    public static int TREE_NUMBER_LENGTH = 4;

    @Transient
    public static String long2hex(long num) {
        BigInteger big = BigInteger.valueOf(num);
        String hex = big.toString(Character.MAX_RADIX);
        return StringUtils.leftPad(hex, TREE_NUMBER_LENGTH, '0');
    }

    @Transient
    public static long hex2long(String hex) {
        BigInteger big = new BigInteger(hex, Character.MAX_RADIX);
        return big.longValue();
    }

    @Transient
    public String getDisplayName() {
        StringBuilder sb = new StringBuilder();
        Org org = this;
        sb.append(org.getName());
        org = org.getParent();
        while (org != null) {
            sb.insert(0, " - ");
            sb.insert(0, org.getName());
            org = org.getParent();
        }
        return sb.toString();
    }

    @Transient
    public long getTreeMaxLong() {
        BigInteger big = new BigInteger(getTreeMax(), Character.MAX_RADIX);
        return big.longValue();
    }

    @Transient
    public void addChild(Org bean) {
        List<Org> list = getChildren();
        if (list == null) {
            list = new ArrayList<Org>();
            setChildren(list);
        }
        list.add(bean);
    }

    @Transient
    public void applyDefaultValue() {
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Org)) {
            return false;
        }
        Org that = (Org) o;
        return Objects.equal(id, that.id);
    }

    private Integer id;

    private Set<UserOrg> userOrgs = new HashSet<UserOrg>(0);
    private Set<User> users = new HashSet<User>(0);
    private List<Org> children = new ArrayList<Org>(0);

    private Org parent;

    private String name;
    private String fullName;
    private String description;
    private String contacts;
    private String phone;
    private String fax;
    private String address;
    private String number;
    private String treeNumber;
    private Integer treeLevel;
    private String treeMax;

    @XmlTransient
    @Id
    @Column(name = "f_org_id", unique = true, nullable = false)
    @TableGenerator(name = "tg_cms_org", pkColumnValue = "cms_org", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_org")
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "org")
    public Set<UserOrg> getUserOrgs() {
        return userOrgs;
    }

    public void setUserOrgs(Set<UserOrg> userOrgs) {
        this.userOrgs = userOrgs;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "org")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @XmlTransient
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @OrderBy(value = "treeNumber asc, id asc")
    public List<Org> getChildren() {
        return this.children;
    }

    public void setChildren(List<Org> children) {
        this.children = children;
    }

    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "f_parent_id")
    public Org getParent() {
        return this.parent;
    }

    public void setParent(Org parent) {
        this.parent = parent;
    }

    @Column(name = "f_name", nullable = false, length = 150)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "f_full_name", length = 150)
    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "f_description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "f_contacts", length = 100)
    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    @Column(name = "f_phone", length = 100)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "f_fax", length = 100)
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(name = "f_address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "f_number", length = 100)
    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "f_tree_number", nullable = false, length = 100)
    public String getTreeNumber() {
        return this.treeNumber;
    }

    public void setTreeNumber(String treeNumber) {
        this.treeNumber = treeNumber;
    }

    @Column(name = "f_tree_level", nullable = false)
    public Integer getTreeLevel() {
        return this.treeLevel;
    }

    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    @Column(name = "f_tree_max", nullable = false, length = 10)
    public String getTreeMax() {
        return this.treeMax;
    }

    public void setTreeMax(String treeMax) {
        this.treeMax = treeMax;
    }

}
