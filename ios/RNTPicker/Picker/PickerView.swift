
import UIKit

public class PickerView : UIView {
    
    @objc public var onChange: ((NSDictionary) -> Void)?
    
    @objc public var items = [NSDictionary]() {
        didSet {
            refreshIfNeeded(items: oldValue)
        }
    }
    
    @objc public var selectedIndex = -1 {
        didSet {
            
            DispatchQueue.main.async {
                
                let selectedIndex = self.selectedIndex
                
                guard selectedIndex != self.pickerView.selectedRow(inComponent: 0) else {
                    return
                }
                
                if selectedIndex >= 0 && selectedIndex < self.items.count {
                    self.pickerView.selectRow(selectedIndex, inComponent: 0, animated: true)
                }
                
            }
            
        }
    }

    @objc public var color = UIColor.black {
        didSet {
            refreshIfNeeded(items: items)
        }
    }
    
    @objc public var font = UIFont.systemFont(ofSize: 16) {
       didSet {
           refreshIfNeeded(items: items)
       }
    }
    
    @objc public var textAlign = NSTextAlignment.center {
       didSet {
           refreshIfNeeded(items: items)
       }
    }
    
    @objc public var rowHeight: CGFloat = 44 {
        didSet {
            refreshIfNeeded(items: items)
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
    
    private func refreshIfNeeded(items: [NSDictionary]) {
        if items.count > 0 {
            DispatchQueue.main.async {
                self.pickerView.reloadAllComponents()
            }
        }
    }
    
}

extension PickerView : UIPickerViewDelegate {
    
    public func pickerView(_ pickerView: UIPickerView, widthForComponent component: Int) -> CGFloat {
        return bounds.width
    }

    public func pickerView(_ pickerView: UIPickerView, rowHeightForComponent component: Int) -> CGFloat {
        return rowHeight
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
        optionLabel.textAlignment = textAlign
        
        if items.count >= row {
            optionLabel.text = items[row]["text"] as? String
        }
        else {
            optionLabel.text = "undefined"
        }
        
        return optionLabel
        
    }

    public func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        selectedIndex = row
        if let onChange = onChange, items.count > row {
            let dict = NSMutableDictionary()
            dict["index"] = row
            dict["value"] = items[row]["value"]
            onChange(dict)
        }
    }
    
}

extension PickerView : UIPickerViewDataSource {
    
    public func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    public func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return items.count
    }
    
}
