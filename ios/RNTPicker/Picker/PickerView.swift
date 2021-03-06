
import UIKit

public class PickerView : UIView {
    
    @objc public var onChange: ((NSDictionary) -> Void)?
    
    @objc public var options = [NSDictionary]() {
        didSet {
            refresh()
        }
    }
    
    @objc public var selectedIndex = -1 {
        didSet {

            guard selectedIndex >= 0, selectedIndex < options.count else {
                return
            }
            
            if let onChange = onChange {
                let dict = NSMutableDictionary()
                dict["index"] = selectedIndex
                dict["option"] = options[selectedIndex]
                onChange(dict)
            }
            
            DispatchQueue.main.async {
                
                guard self.selectedIndex != self.pickerView.selectedRow(inComponent: 0) else {
                    return
                }
                
                self.pickerView.selectRow(self.selectedIndex, inComponent: 0, animated: true)
                
            }
            
        }
    }

    @objc public var color = UIColor.black {
        didSet {
            if options.count > 0 {
                refresh()
            }
        }
    }
    
    @objc public var fontSize = 16 {
        didSet {
            font = UIFont.systemFont(ofSize: CGFloat(fontSize))
        }
    }
    
    @objc public var rowHeight = 44 {
        didSet {
            if options.count > 0 {
                refresh()
            }
        }
    }
    
    private var font = UIFont.systemFont(ofSize: 16) {
        didSet {
            if options.count > 0 {
                refresh()
            }
        }
    }
    
    lazy private var pickerView: UIPickerView = {
        
        let view = UIPickerView()
        
        view.translatesAutoresizingMaskIntoConstraints = false
        
        view.dataSource = self
        view.delegate = self
        
        addSubview(view)
        
        addConstraints([
            NSLayoutConstraint(item: view, attribute: .top, relatedBy: .equal, toItem: self, attribute: .top, multiplier: 1, constant: 0),
            NSLayoutConstraint(item: view, attribute: .right, relatedBy: .equal, toItem: self, attribute: .right, multiplier: 1, constant: 0),
            NSLayoutConstraint(item: view, attribute: .bottom, relatedBy: .equal, toItem: self, attribute: .bottom, multiplier: 1, constant: 0),
            NSLayoutConstraint(item: view, attribute: .left, relatedBy: .equal, toItem: self, attribute: .left, multiplier: 1, constant: 0),
        ])
        
        // Workaround for missing selection indicator lines (see https://stackoverflow.com/questions/39564660/uipickerview-selection-indicator-not-visible-in-ios10)
        view.selectRow(0, inComponent: 0, animated: true)
        
        return view
        
    }()
    
    private func refresh() {

        DispatchQueue.main.async {
            
            self.pickerView.reloadComponent(0)
            
            if self.selectedIndex >= 0 && self.selectedIndex < self.options.count {
                self.pickerView.selectRow(self.selectedIndex, inComponent: 0, animated: true)
            }
            else {
                self.selectedIndex = 0
            }
            
        }
        
    }
    
}

extension PickerView : UIPickerViewDelegate {
    
    public func pickerView(_ pickerView: UIPickerView, widthForComponent component: Int) -> CGFloat {
        return bounds.width
    }

    public func pickerView(_ pickerView: UIPickerView, rowHeightForComponent component: Int) -> CGFloat {
        return CGFloat(rowHeight)
    }

    public func pickerView(_ pickerView: UIPickerView, viewForRow row: Int, forComponent component: Int, reusing view: UIView?) -> UIView {
        
        let optionLabel: UILabel
        
        if let label = view as? UILabel {
            optionLabel = label
        }
        else {
            optionLabel = UILabel()
        }
        
        optionLabel.font = font
        optionLabel.textColor = color
        optionLabel.textAlignment = .center
        
        var text = "undefined"
        
        if options.count >= row {
            if let label = options[row]["text"] as? String {
                text = label
            }
        }
        
        optionLabel.text = text
        
        return optionLabel
        
    }

    public func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        selectedIndex = row
    }
    
}

extension PickerView : UIPickerViewDataSource {
    
    public func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    public func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return options.count
    }
    
}
