package com.vgg.fvp.vendor;

import com.vgg.fvp.common.data.Role;
import com.vgg.fvp.common.data.RoleRepository;
import com.vgg.fvp.common.data.User;
import com.vgg.fvp.common.data.UserRepository;
import com.vgg.fvp.vendor.dto.VendorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class VendorServiceImpl implements VendorService {

    private VendorRepository repo;
    private RoleRepository roleRepo;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepo;

    @Autowired
    public VendorServiceImpl(VendorRepository repo, RoleRepository roleRepo, PasswordEncoder passwordEncoder, UserRepository userRepo) {
        this.repo = repo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public Vendor create(VendorDTO vendor) {
        return repo.save(mapper(vendor));
    }

    @Override
    public Optional<Vendor> getVendor(Long id) {
        return repo.findById(id);
    }

    @Override
    public Vendor update(Long id, Vendor vendor) {
        Optional<Vendor> existingVendor = getVendor(id);
        if(existingVendor != null){
            vendor.setId(existingVendor.get().getId());
            repo.save(vendor);
        }
        return vendor;
    }

    @Override
    public void delete(Long id) {
        Optional<Vendor> vendor = getVendor(id);
        if(vendor.isPresent()){
            repo.delete(vendor.get());
        }

    }

    @Override
    public Vendor addUser(Vendor vendor, String password) {
        User user = createUser(vendor.getEmail(), password);
        vendor.setUser(user);
        return repo.save(vendor);
    }

    @Override
    public Page<Vendor> getAllVendors(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return repo.findAll();
    }

    public Vendor mapper(VendorDTO vendorDTO){
        Vendor vendor = new Vendor();

        vendor.setBusinessName(vendorDTO.getBusinessName());
        vendor.setEmail(vendorDTO.getEmail());
        vendor.setPhoneNumber(vendorDTO.getPhoneNumber());

        return vendor;
    }

    public User createUser(String email, String password){
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        Role role = roleRepo.findRoleByName(Role.UserType.VENDOR.getType());
        user.setRole(role);
        userRepo.save(user);
        return user;
    }
}
